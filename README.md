# Text-Scraper
This is the backend text scraper that serves comments to the [StreamYardIntegration frontend](https://github.com/efwoods/StreamYardIntegration).

## What is used to make this?
It is a Spring Boot REST API that implements automated headless browsing using selenium. 

## How do I use this?
To use this backend, perform the following:

1. Build the java jar with gradle. 
2. Use a GET request to call the endpoint /jsoup to test scraping wikipedia.
3. Use a GET request to call the endpoint /scrape, and send a url, username, password, and xpath as query parameters to scrape the content of that website.
4. Use a GET request to call the endpoint /comments to view the scraped content in HAL format

## Sponsor this project
This project is currently in the development phase. Sponsorships are not expected. If you would like to do so, [please feel free to sponsor this project](https://github.com/sponsors/efwoods?o=esb).
