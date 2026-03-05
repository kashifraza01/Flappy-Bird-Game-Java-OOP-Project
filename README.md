# Flappy Bird Java

A classic **Flappy Bird** clone developed in **Java** using **Swing** for GUI, **AWT** for graphics, and **Timers** for game mechanics. This project demonstrates basic game development concepts like animation, collision detection, scoring, and object-oriented programming in Java.

## Features

* Classic Flappy Bird gameplay
* Multiple difficulty levels: **Easy**, **Medium**, **Hard**
* Smooth bird movement with gravity and jumping mechanics
* Randomly generated pipes with gaps
* Collision detection and game over handling
* Score and high score tracking
* Restart functionality using **SPACE** key

---

## Screenshots

| Game Running                             | Game Over                                 |
| ---------------------------------------- | ----------------------------------------- |
| ![Screenshot1](./ss1.png) | ![Screenshot2](./ss2.png) |

---

## How to Play

1. Run the game using your favorite Java IDE or command line.
2. Press **SPACE** to make the bird jump.
3. Avoid hitting pipes or going off-screen.
4. Score points by passing through pipes.
5. When the game ends, press **SPACE** to restart.

---

## Requirements

* Java **JDK 8+**
* Swing and AWT (built-in Java libraries)
* Images located in the project folder:

  * `flappybirdbg.png`
  * `flappybird.png`
  * `toppipe.png`
  * `bottompipe.png`

---

## Running the Game

1. Clone the repository:

```bash
git clone https://github.com/kashifraza01/Flappy-Bird-Game-Java-OOP-Project.git
```

2. Compile the code:

```bash
javac flappygame/FlappyBird.java
```

3. Run the game:

```bash
java flappygame.FlappyBird
```

---

## Project Structure

```
FlappyBirdJava/
├─ flappygame/
│  ├─ FlappyBird.java       # Main game code
│  ├─ flappybirdbg.png      # Background image
│  ├─ flappybird.png        # Bird image
│  ├─ toppipe.png           # Top pipe image
│  └─ bottompipe.png        # Bottom pipe image
└─ README.md
```

---

## How It Works

* **Bird**: Controlled using keyboard input (SPACE). Gravity pulls the bird down, and SPACE makes it jump.
* **Pipes**: Randomly generated at regular intervals and move leftwards.
* **Collision Detection**: Checks for intersections between the bird and pipes or screen boundaries.
* **Scoring**: Bird earns points for passing through pipes.
* **Difficulty**: Determines pipe speed and gap sizes.
