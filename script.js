let secretNumber = Math.floor(Math.random() * 100) + 1;

let attempts = 0;


function checkGuess() {

    let input = document.getElementById("guessInput");
    let guess = Number(input.value);

    let message = document.getElementById("message");
    let previousGuesses = document.getElementById("previousGuesses");

    if (guess < 1 || guess > 100) {
        message.textContent = "Guess a number between 1 and 100!";
        return;
    }

    attempts++;

    document.getElementById("attempts").textContent = attempts;

    let guessItem = document.createElement("p");

    if (guess === secretNumber) {
        guessItem.textContent = guess + " → 🎉 Correct!";
        guessItem.classList.add("correct");

        message.textContent =
            "🎉 Correct! You guessed it in " + attempts + " attempts!";
    }
    else if (guess < secretNumber) {
        guessItem.textContent = guess + " → H";
        guessItem.classList.add("higher");

        message.textContent = "📈 Higher";
    }
    else {
       guessItem.textContent = guess + " → L";
        guessItem.classList.add("lower");

        message.textContent = "📉 Lower";
    }

    previousGuesses.appendChild(guessItem);
    input.value = "";
    input.focus();
}

function restartGame() {
    secretNumber = Math.floor(Math.random() * 100) + 1;
    attempts = 0;
    document.getElementById("attempts").textContent = 0;
    document.getElementById("message").textContent = "Good luck! 🍀";
    document.getElementById("guessInput").value = "";
    document.getElementById("previousGuesses").innerHTML = "";
    document.getElementById("guessInput").focus();
}

function addNumber(number) {
    let input = document.getElementById("guessInput");
    input.value += number;
    input.focus();
}

function clearInput() {
    document.getElementById("guessInput").value = "";
    document.getElementById("guessInput").focus();
}

document.getElementById("guessInput").addEventListener("keydown", function(event) {

    if (event.key === "Enter") {
        checkGuess();
    }
});

let input = document.getElementById("guessInput");
if (window.innerWidth <= 700) {
    input.readOnly = true;
}
