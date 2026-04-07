# Subscription Management System

A simple Java project built for a functional programming workshop.

## Project Overview

This project simulates a subscription list management system.  
It allows managing subscribers, filtering them with different business rules, and applying actions such as extending subscriptions or deactivating accounts.

## Features

- Create and manage subscribers
- Store subscribers using a DAO class
- Filter subscribers using functional interfaces
- Apply actions to matching subscribers
- Use Lambda expressions for business rules
- Test core logic using JUnit 5

## Technologies Used

- Java 17
- Maven
- JUnit 5

## Project Structure

- `Plan` → subscription plan enum
- `Subscriber` → subscriber model
- `SubscriberDAO` → stores and retrieves subscribers
- `SubscriberFilter` → functional interface for filtering
- `SubscriberAction` → functional interface for actions
- `SubscriberProcessor` → processes filtering and actions
- `BusinessRules` → contains business rules using lambda expressions
- `Main` → runs the application
- `SubscriberProcessorTest` → unit tests

## Business Rules

The project includes the following rules:

- Active subscriber
- Expiring subscription
- Active and expiring subscriber
- Subscriber by plan
- Paying subscriber
- Extend subscription
- Deactivate subscriber

## Test Scenarios

The project includes JUnit tests for:

- Active subscribers
- Expiring subscriptions
- Active and expiring subscribers
- Extending subscriptions for paying subscribers
- Deactivating expired free subscribers

