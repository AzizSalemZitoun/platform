import sys
import json
import warnings
import os
import torch
from transformers import AutoTokenizer, AutoModelForCausalLM

warnings.filterwarnings("ignore")
os.environ["TRANSFORMERS_VERBOSITY"] = "error"

HF_TOKEN = "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"

MODEL_NAME = "google/gemma-2-2b-it"
MAX_NEW_TOKENS = 128

device = "cuda" if torch.cuda.is_available() else "cpu"

def load_model_and_tokenizer():
    print(f">>> Loading model: {MODEL_NAME}", file=sys.stderr)
    model = AutoModelForCausalLM.from_pretrained(
        MODEL_NAME,
        device_map="auto" if torch.cuda.is_available() else None,
        torch_dtype=torch.float16 if torch.cuda.is_available() else torch.float32,
        token=HF_TOKEN
    )
    tokenizer = AutoTokenizer.from_pretrained(MODEL_NAME, token=HF_TOKEN)
    if tokenizer.pad_token is None:
        tokenizer.pad_token = tokenizer.eos_token
    model.to(device)
    print(f">>> Model {MODEL_NAME} loaded on {device}!", file=sys.stderr)
    return tokenizer, model

tokenizer, model = load_model_and_tokenizer()

def generate_with_model(prompt: str, max_tokens: int) -> str:
    inputs = tokenizer(prompt, return_tensors="pt").to(device)
    try:
        outputs = model.generate(
            **inputs,
            max_new_tokens=max_tokens,
            do_sample=True,
            temperature=0.7,
            top_p=0.9
        )
        out = tokenizer.decode(outputs[0], skip_special_tokens=True)
        if out.startswith(prompt):
            out = out[len(prompt):]
        return out.strip()
    except Exception as e:
        raise RuntimeError(f"Model generation failed: {e}") from e

def main():
    input_json = sys.stdin.read()
    try:
        data = json.loads(input_json)
    except json.JSONDecodeError as e:
        raise RuntimeError(f"Invalid JSON input: {e}")

    report_prompt = f"""
You are an expert compliance analyst. Provide a concise report without final decision.
Acceptance Conditions (JSON):
{json.dumps(data['conditions'], separators=(',', ':'))}
Client: {data['client_name']}, Project: {data['client_name']}
Document: {data['documents']}
Report:
"""
    report = generate_with_model(report_prompt, MAX_NEW_TOKENS)
    print(report)

if __name__ == "__main__":
    main()
