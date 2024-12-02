import requests

backend = 'http://192.168.0.2:30321/check_score.php?score=10000000000000000000'
index = requests.get('http://192.168.0.2:30321')
index = requests.get(backend)
index = requests.get('http://192.168.0.2:30321')
print(index.text)