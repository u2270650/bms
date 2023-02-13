## Assignment 1: Individual 50% | Thursday, 23/02/2023

### Title: Booking Management System (BMS)
### Background:
A local college has decided to rent out its rooms during evenings, weekends, and holidays as a way to generate extra income.
It hopes that local clubs and societies may be interested in booking rooms for meetings, rehearsals, to play games, etc.
Management has decided that a software system is required to allow three Booking Clerks and one Room Manager to view the state of the system at all times.
You have been asked to develop a prototype desktop system that uses a shared memory model (i.e. a concurrent system) to provide the functionality required.

** TIMELY COMMITS ON GITHUB
** APPROPRIATE COMMENTS
** USE CASE DIAGRAM
** CLASS DIAGRAM

### Requirements:
* Entities
    - Rooms
        - no. of seats
        - type
        - availability
        - blackout days & reason for blackout
    - Bookings
        - person
        - organisation
        - contact details
        - notes
        - from & to date
    - Users
        - name
        - login
        - password

* User Levels
    1) Booking Clerks:
        - Availibility of rooms
        - Make Bookings
        - Filter Rooms (type, dates available)

    2) Room Manager:
        - Add/Remove rooms
        - Add notes for blackout days & time 	  period for blackout

## Reference Links:
- [cheatsheet](https://dzone.com/refcardz/getting-started-javafx)
- [JFX documentation](https://openjfx.io/)

```sh
git log --pretty=format:"%C(auto)%h%x09%an%x09%ad%x09%Cblue%s" // git log
```
