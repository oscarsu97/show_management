# show_management

## Getting Started
Compile the java code
```
javac Main.java
```

Run the application
```
java Main
```

## Commands
### Setup
Setup [Show Number] [Number of Rows] [Number of seats per row] [Cancellation window in minutes]
```
Setup 1 3 3 10
```

### View
View [Show Number]
```
View 1
```
![image](https://github.com/oscarsu97/show_management/assets/50538208/b1ed9436-ef5b-45f2-897d-7571165bb9bf)

### Availability
Availability [Show Number]
```
Availability 1
```
![image](https://github.com/oscarsu97/show_management/assets/50538208/442fc7a2-905f-49c1-8dc9-cb2b2d8a8a79)

### Book
Book [Show Number] [Phone#] [Comma separated list of seats]
```
Book 1 123 A1,B1
```
![image](https://github.com/oscarsu97/show_management/assets/50538208/ffddef05-b306-4d70-864c-b649ee81a6ae)


### Cancel
Cancel [Ticket#][Phone#]
```
Cancel 2 234
![image](https://github.com/oscarsu97/show_management/assets/50538208/8e3381e1-578e-4caa-a434-2142bc68abab)

## Cancellation Window
If it is passed the cancellation window set during the `setup` command, ticket cannot be canceled and will be shown as such:
![image](https://github.com/oscarsu97/show_management/assets/50538208/7125bd91-ddd6-475c-a1db-aceb0b3a2400)

