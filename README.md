<h1 align="center">
ShareIt (pet project)
</h1>

'ShareIt' is a test project done while studying on [Yandex.Practicum](https://practicum.yandex.ru) platform.
It represents back-end logics for an item sharing service.

## Structure:

Project consists of two modules:

* server (main module)
* gateway (responsible for incoming data validation)

## Features:

* users can add items and offer them to others
* users can search for items by their name or description
* users can send/reject/approve/modify their bookings
* users can create requests if item they need is missing
* users can post commentaries after booking has finished
* incoming data is validated by a dedicated service
* other minor features

## Tech stack:

Spring Boot, Maven, Hibernate, Lombok, PostgreSQL, H2, Docker
92% code coverage by JUnit and Mockito tests

## Launch:

App can be launched by building and running three docker images (via docker-compose.yml):

* server
* gateway
* db