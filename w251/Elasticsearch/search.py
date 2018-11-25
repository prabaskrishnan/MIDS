import json
import sys
import requests

def main():
    search_term=sys.argv[1]
    search_url = 'http://xxx.xx.xx.xx:9200/agri/india'
    response = requests.get(search_url + '/_search?q=' + search_term + '&pretty')
    if response.status_code == 200:
        results = json.loads(response.text)
        print('\n')
        for item in results['hits']['hits']:
            print(item['_source']['title'])
            print (item['_source']['url'])
            print('\n')
            print (item['_source']['description'])
            print('______________________________________________________________________________________________________________')

    else:
        print('No results found.')

main()
