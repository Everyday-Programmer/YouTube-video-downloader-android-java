FROM python:3.9-slim

WORKDIR /app

COPY . /app

RUN pip install --no-cache-dir Flask yt-dlp

EXPOSE 5000

ENV FLASK_APP=ap.py

CMD ["flask", "run", "--host=0.0.0.0"]