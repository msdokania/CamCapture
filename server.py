import base64
import os, os.path
from time import strftime

import flask
from flask import Flask, request, make_response, jsonify
from datetime import date

# Create a flask instance
app = Flask(__name__)

@app.route('/', methods=['POST'])
def upload_image():
        imagestring = flask.request.form['encodedImage']
        category = flask.request.form['category']
        image = base64.b64decode(imagestring)
        imagefileName = "IMG"+".jpg"

        with safe_open_path(category+"/"+imagefileName) as f:
                f.write(image)
        f.close()
        message = {'message':'Done','code':'SUCCESS'}
        return make_response(jsonify(message), 201)

def safe_open_path(path):
        os.makedirs(os.path.dirname(path), exist_ok=True)
        return open(path, 'wb')


if __name__ == '__main__':
        #app.run()
        app.run("127.0.0.1", port=5000, debug=True)



# See PyCharm help at https://www.jetbrains.com/help/pycharm/
