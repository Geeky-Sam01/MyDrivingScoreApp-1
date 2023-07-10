'''def calculate_driving_score(user_id, date):
    # Your code to calculate the driving score goes here
    driving_score  =100
    # Return the calculated driving score
    return driving_score
'''
import pandas as pd
# loading data from google sheet
gsheet_id = '1vCuNwKm81LZ7xjYUc73Ooj-BMP3Wf5dac8FQDS9XatQ'
gsheet_name = 'Sensor_Items'
gsheet_url = 'https://docs.google.com/spreadsheets/d/{}/gviz/tq?tqx=out:csv&sheet={}'.format(
  gsheet_id, gsheet_name)

columns_to_select = ['Date', 'Time', 'Device ID', 'Speed (m/s)']
df = pd.read_csv(gsheet_url,
                 usecols=columns_to_select,
                 parse_dates={'datetime': ['Date', 'Time']})


# Harsh accel and harsh breaking count
def rashDrivingCount(filtered_df):
  count_haccel = 0
  count_hbreak = 0
  speeds = filtered_df['Speed (m/s)'].reset_index(drop=True)

  for i, speed in speeds.items():
    if int(i) >= 2:
      pair1 = (speeds[i - 2], speeds[i - 1])
      pair2 = (speeds[i - 1], speed)

      if max(pair2) - min(pair1) > 10:  # Threshold for harsh acceleration
        count_haccel += 1
      if min(pair2) - max(pair1) > 10:  # Threshold for harsh braking
        count_hbreak += 1

  return [count_hbreak, count_haccel]


#filter data based on user id and date
def filter_df(df, device_id, date):
  filtered_df = df[(df['Device ID'] == device_id)
                   & (df['datetime'].dt.date == date.date())]
  return filtered_df


'''Some error is occuring when a new userID is coming.
In local it is running fine , but on replit deployment , 
for every new user I am having to stop and re-run the deployment.'''


def calculateDrivingScore(count_list):
  weight_haccel = 0.6
  weight_hbreak = 0.4
  driving_score = 100

  to_subtract = weight_haccel * count_list[1] + weight_hbreak * count_list[0]
  driving_score -= to_subtract

  # Ensure the driving score is within a certain range
  driving_score = max(driving_score, 0)
  driving_score = min(driving_score, 100)

  return driving_score


def get_driving_scores(user_id, date):
  global driving_scores_per_day  # Make driving_scores_per_day a global variable

  filtered_result = filter_df(df, user_id, pd.to_datetime(date))

  if filtered_result.empty:
    print("No data found for the given user ID and date.")
    return -1  # Return -1 when there is no data

  count = rashDrivingCount(filtered_result)
  score = calculateDrivingScore(count)

  return score if score is not None else "-1"  # Return -1 if the score is None


'''FOR TESTING DONT REMOVE THIS CODE
#driving_scores_per_day = np.array([])

#date = input("Enter a date (YYYY-MM-DD): ")
#user_id = input("Enter user ID: ")

#score = get_driving_scores(user_id, date)
#print(f"Driving Score on date: {date} is {score}")
#print("Overall Driving Score:", overall_driving_score)
#9579b8edbc787587 -> Oishik
'''
