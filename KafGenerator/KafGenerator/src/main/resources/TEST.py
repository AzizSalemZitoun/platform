import sys
import json
import warnings
import os
import torch
from transformers import AutoTokenizer, AutoModelForCausalLM, BitsAndBytesConfig

warnings.filterwarnings("ignore")
os.environ["TRANSFORMERS_VERBOSITY"] = "error"

HF_TOKEN = "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"

MODEL_NAME = "Qwen/Qwen3-4B-Instruct-2507"
MAX_NEW_TOKENS = 256

def load_model_and_tokenizer():
    print(f">>> Loading 4-bit quantized model: {MODEL_NAME}", file=sys.stderr)
    bnb_config = BitsAndBytesConfig(
        load_in_4bit=True,
        bnb_4bit_use_double_quant=True,
        bnb_4bit_quant_type="nf4",
        bnb_4bit_compute_dtype=torch.float32
    )
    model = AutoModelForCausalLM.from_pretrained(
        MODEL_NAME,
        quantization_config=bnb_config,
        device_map="auto",
        use_auth_token=HF_TOKEN
    )
    tokenizer = AutoTokenizer.from_pretrained(MODEL_NAME, use_auth_token=HF_TOKEN)
    if tokenizer.pad_token is None:
        tokenizer.pad_token = tokenizer.eos_token
    print(f">>> 4-bit Model {MODEL_NAME} loaded!", file=sys.stderr)
    return tokenizer, model

tokenizer, model = load_model_and_tokenizer()

def generate_with_model(prompt: str, max_tokens: int) -> str:
    inputs = tokenizer(prompt, return_tensors="pt", padding=True)
    input_ids = inputs["input_ids"].to(model.device)
    attention_mask = inputs["attention_mask"].to(model.device)
    try:
        output_ids = model.generate(
            input_ids,
            attention_mask=attention_mask,
            max_new_tokens=max_tokens,
            do_sample=True,
            temperature=0.7,
            top_p=0.9
        )
        out = tokenizer.decode(output_ids[0], skip_special_tokens=True)
        if out.startswith(prompt):
            out = out[len(prompt):]
        return out.strip()
    except Exception as e:
        raise RuntimeError(f"Model generation failed: {e}") from e

def main():
    raw_input = sys.stdin.read()
    data = json.loads(raw_input)

    summary_prompt = f"Summarize the following text concisely:\n\n{data['documents']}\n\nSummary:"
   
    summary = generate_with_model(summary_prompt, MAX_NEW_TOKENS)

    report_prompt = f"""
You are an expert compliance analyst. Provide a concise report without final decision.
Acceptance Conditions (JSON):
{json.dumps(data['conditions'], separators=(',', ':'))}
Project: {data['client_name']}
Document: {data['documents']}
Client Summary: {summary}
Report:
"""
    
    report = generate_with_model(report_prompt, MAX_NEW_TOKENS)

    result = {
        "summary": summary,
        "report": report
    }
    print(json.dumps(result, ensure_ascii=False))

if __name__ == "__main__":
    main()
