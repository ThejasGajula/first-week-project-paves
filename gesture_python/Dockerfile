FROM python:3.10-slim

WORKDIR /app

COPY requirements.txt .
RUN apt-get update && apt-get install -y libgl1 libglib2.0-0 && pip install -r requirements.txt

COPY . .

CMD ["python", "app.py"]
