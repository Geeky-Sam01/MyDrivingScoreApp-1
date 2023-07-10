from flask import Flask, request, jsonify
from driving_score import get_driving_scores
from flask import render_template

app = Flask(__name__)


@app.route('/')
def index():
  return render_template('index.html')


# GET endpoint to fetch driving score
@app.route('/driving-score', methods=['GET'])
def get_driving_score():
  user_id = request.args.get('userID')  #,default='9579b8edbc787587')
  date = request.args.get('date')  #,default='2023-05-25')

  #driving_score = get_driving_scores(user_id, date)
  '''return jsonify({
    'userID': user_id,
    'date': date,
    'driving_score': driving_score
  })'''
  data = {
    "User ID": user_id,
    "Date": date,
    "short": {
      "trip1": {
        "driving score": 100,
        "dist": "10km",
        "time": "00:00:00"
      },
      "trip2": {
        "driving score": 100,
        "dist": "10km",
        "time": "00:00:00"
      }
    },
    "medium": {
      "trip1": {
        "driving score": 100,
        "dist": "100km",
        "time": "00:00:00"
      },
      "trip2": {
        "driving score": 100,
        "dist": "100km",
        "time": "00:00:00"
      }
    },
    "long": {
      "trip1": {
        "driving score": 100,
        "dist": "1000km",
        "time": "00:00:00"
      },
      "trip2": {
        "driving score": 100,
        "dist": "1000km",
        "time": "00:00:00"
      }
    },
    "Overall Driving Score": 90
  }
  response = jsonify(data)
  return response


#@app.route('/userID', methods=['GET'])
#def get_overallDrivingScore():
#  user_id = request.args.get('userID')

if __name__ == '__main__':
  app.run(debug=True, host='0.0.0.0')
