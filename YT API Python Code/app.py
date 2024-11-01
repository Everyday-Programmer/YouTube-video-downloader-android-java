from flask import Flask, request, jsonify
import yt_dlp

app = Flask(__name__)

def get_download_url(url):
    try:
        ydl_opts = {
            'format': 'best',
            'noplaylist': True
        }

        with yt_dlp.YoutubeDL(ydl_opts) as ydl:
            info = ydl.extract_info(url, download = False)
            return jsonify({
                "title": info.get('title'),
                "download_url": info.get('url')
            })
    except Exception as e:
        return str(e)


@app.route('/download_url', methods=['POST'])
def download_url():
    data = request.json
    video_url = data.get('url')

    if not video_url:
        return jsonify({"error": "URL is required"}), 400

    try:
        res = get_download_url(video_url)

        if res:
            return res, 200
        else:
            return jsonify({"error": "Failed to fetch download url"}), 500
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/video_info', methods=['POST'])
def video_info():
    data = request.get_json()
    video_url = data.get('url')

    if not video_url:
        return jsonify({"error": "URL is required"}), 400

    try:
        ydl_opts = {
            'format': 'best',
            'noplaylist': True,
            'quiet': True
        }

        with yt_dlp.YoutubeDL(ydl_opts) as ydl:
            video_info = ydl.extract_info(video_url, download=False)

        video_details = {
            "title": video_info.get("title"),
            "thumbnail": video_info.get("thumbnail"),
            "uploader": video_info.get("uploader"),
            "duration": video_info.get("duration"),
            "view_count": video_info.get("view_count"),
            "like_count": video_info.get("like_count"),
            "dislike_count": video_info.get("dislike_count"),
            "description": video_info.get("description"),
            "upload_date": video_info.get("upload_date"),
            "url": video_info.get("url")
        }

        return jsonify(video_details), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)