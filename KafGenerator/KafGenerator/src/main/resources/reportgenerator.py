import sys
import json
import torch
import warnings
import os
from transformers import AutoTokenizer, AutoModelForCausalLM, pipeline
from huggingface_hub import login

# --- Auth (optional if using public models) ---
login("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")

warnings.filterwarnings("ignore")

# --- Hardware ---
device = "cuda" if torch.cuda.is_available() else "cpu"

# --- Models ---
# Primary (as requested): Mistral 7B Instruct
PREFERRED_MODEL = "mistralai/Mistral-7B-Instruct-v0.2"
# Fallback for demos if Mistral can't load on this machine
FALLBACK_MODEL = "microsoft/phi-3-mini-4k-instruct"

def load_mistral_or_fallback():
    """
    Try to load Mistral 7B Instruct with 4-bit quantization (low RAM).
    If it fails (e.g., bitsandbytes on Windows), fallback to Phi-3 mini.
    """
    # Attempt 4-bit load first (best chance to fit locally)
    try:
        from transformers import BitsAndBytesConfig
        bnb_config = BitsAndBytesConfig(
            load_in_4bit=True,
            bnb_4bit_quant_type="nf4",
            bnb_4bit_use_double_quant=True,
            bnb_4bit_compute_dtype=torch.float16 if device == "cuda" else torch.bfloat16
        )

        tokenizer = AutoTokenizer.from_pretrained(PREFERRED_MODEL, use_fast=True)
        model = AutoModelForCausalLM.from_pretrained(
            PREFERRED_MODEL,
            quantization_config=bnb_config,
            device_map="auto",
            trust_remote_code=True
        )
        return tokenizer, model, PREFERRED_MODEL
    except Exception as e:
        print(f"⚠️ Mistral 7B 4-bit load failed: {e}", file=sys.stderr)
        print("→ Falling back to Phi-3 mini for demo so you still get output.", file=sys.stderr)

    # Fallback (no bitsandbytes required)
    try:
        tokenizer = AutoTokenizer.from_pretrained(FALLBACK_MODEL, use_fast=True)
        model = AutoModelForCausalLM.from_pretrained(
            FALLBACK_MODEL,
            device_map="auto" if device == "cuda" else None,
            torch_dtype=torch.float16 if device == "cuda" else torch.float32,
            trust_remote_code=True
        )
        return tokenizer, model, FALLBACK_MODEL
    except Exception as e:
        print(f"❌ Fallback model load failed too: {e}", file=sys.stderr)
        raise

tokenizer, model, active_model = load_mistral_or_fallback()

# --- Text generation pipeline ---
# return_full_text=False avoids the usual "echoed prompt" mess.
generator = pipeline(
    "text-generation",
    model=model,
    tokenizer=tokenizer,
    device=0 if device == "cuda" else -1
)

def chunk_text(text, max_tokens=240):
    """Split text into manageable chunks to avoid truncation."""
    import re
    # crude token proxy = words; adjust if needed
    sentences = re.split(r'(?<=[.!?])\s+', text.strip())
    chunks, current, count = [], "", 0
    for s in sentences:
        w = len(s.split())
        if count + w > max_tokens and current:
            chunks.append(current.strip())
            current, count = s, w
        else:
            current = (current + " " + s).strip() if current else s
            count += w
    if current:
        chunks.append(current.strip())
    return chunks

def _gen(prompt, max_new_tokens=320, temperature=0.3, top_p=0.9, repetition_penalty=1.05):
    out = generator(
        prompt,
        max_new_tokens=max_new_tokens,
        do_sample=True,
        temperature=temperature,
        top_p=top_p,
        repetition_penalty=repetition_penalty,
        return_full_text=False
    )
    return out[0]["generated_text"].strip()

def summarize_text(text: str) -> str:
    """Summarize while preserving facts, numbers, dates."""
    chunks = chunk_text(text)
    summaries = []
    for c in chunks:
        prompt = (
            "You are a precise summarizer. Summarize the text below while preserving "
            "all facts, numbers, and dates. Be concise.\n\n"
            f"### Text:\n{c}\n\n### Summary:"
        )
        summaries.append(_gen(prompt, max_new_tokens=220, temperature=0.2, top_p=0.9))
    # Optional second-pass compression if many chunks
    merged = " ".join(summaries)
    if len(summaries) > 1:
        prompt2 = (
            "Merge the following partial summaries into a single concise summary, "
            "keeping all critical facts, numbers, and dates accurate.\n\n"
            f"{merged}\n\n### Merged Summary:"
        )
        return _gen(prompt2, max_new_tokens=240, temperature=0.2, top_p=0.9)
    return merged

def generate_report(summary: str, conditions: dict, client_name: str, project_description: str) -> str:
    """
    Generate the structured compliance report similar to your sample:
    - Executive Summary
    - Condition-Based Analysis with PASS/FAIL + proof
    - Final Conclusion (ACCEPT/REJECT)
    """
    # Make the model stick to the structure with explicit instructions & delimiters.
    prompt = f"""
You are an expert compliance analyst. Using the client summary and the acceptance conditions,
write a professional compliance report with the EXACT structure below. Always cite proof
verbatim from the provided content when possible (short quotes). If data is missing, mark FAIL
and state that evidence is absent.

### Acceptance Conditions (JSON):
{json.dumps(conditions, indent=2, ensure_ascii=False)}

### Client Information:
- Client Name: {client_name}
- Project Description: {project_description}

### Documents Summary:
{summary}

### REQUIRED OUTPUT FORMAT (STRICT):
**Compliance Report for {client_name}**

**1. Executive Summary:**
[2–5 sentences concisely stating overall findings and key blockers, if any.]

**2. Condition-Based Analysis:**
For each condition, write:
[PASS/FAIL] - [1–2 sentence justification with brief quote or data point as proof. If missing, say so.]

**3. Final Conclusion:**
[ACCEPT/REJECT] - [1–2 sentence rationale based on the above.]

Only output the report—no extra commentary.
"""
    # Give Mistral a bit more room; keep it bounded for demo stability.
    return _gen(prompt, max_new_tokens=540, temperature=0.25, top_p=0.9, repetition_penalty=1.08)

if __name__ == "__main__":
    raw_input = sys.stdin.read()
    data = json.loads(raw_input)

    documents_text = data["documents"]
    conditions = data["conditions"]
    client_name = data["client_name"]
    project_description = data["project_name"]

    summary = summarize_text(documents_text)
    report = generate_report(summary, conditions, client_name, project_description)

    print(report)
    sys.stdout.flush()
