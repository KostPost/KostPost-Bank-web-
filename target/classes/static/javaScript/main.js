document.addEventListener("DOMContentLoaded", function () {
    document.cookie.split(";").forEach(function (c) {
        document.cookie = c.trim().split("=")[0] + "=;expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/";
    });
});
document.addEventListener('DOMContentLoaded', function () {
    updateInputField();
});
document.querySelectorAll('input[name="identifierType"]').forEach((input) => {
    input.addEventListener('change', function () {
        const inputField = document.getElementById('card-number');
        if (this.value === 'username') {
            inputField.placeholder = "Имя пользователя";
            inputField.name = "username";
        } else {
            inputField.placeholder = "Номер карты";
            inputField.name = "card-number";
        }
    });
});

document.addEventListener('DOMContentLoaded', function () {
    const buttons = document.querySelectorAll('.custom-btn');
    const windows = {
        'transfer-btn': document.getElementById('transfer-window'),
        'deposits-btn': document.getElementById('deposits-window'),
        'currency-btn': document.getElementById('currency-window'),
    };
    let lastOpenedWindow = null;
    const historyElement = document.getElementById('gray-rectangle'); // Идентификатор элемента с историей транзакций
    const transactionDetailsElement = document.getElementById('operation-details'); // Идентификатор элемента с информацией о транзакции

    buttons.forEach(button => button.addEventListener('click', function () {
        const windowToShow = windows[button.id];
        if (windowToShow.style.display === 'none') {
            if (lastOpenedWindow) {
                lastOpenedWindow.style.display = 'none';
            }
            windowToShow.style.display = 'block';
            lastOpenedWindow = windowToShow;
            historyElement.style.display = 'none';
            transactionDetailsElement.style.display = 'none';
        } else {
            windowToShow.style.display = 'none';
            lastOpenedWindow = null;
            historyElement.style.display = 'block';
            transactionDetailsElement.style.display = 'block';
        }
    }));
});

document.getElementById('card-number').addEventListener('input', function (e) {
    let input = e.target.value.replace(/\D/g, ''); // Удаляем нечисловые символы
    input = input.substring(0, 16); // Ограничиваем длину до 16 символов
    let formattedInput = '';
    for (let i = 0; i < input.length; i++) {
        if (i > 0 && i % 4 === 0) {
            formattedInput += ' '; // Добавляем пробел каждые 4 цифры
        }
        formattedInput += input[i];
    }
    e.target.value = formattedInput; // Обновляем значение поля ввода
});
document.getElementById("transfer-form").addEventListener("submit", function(e) {
    e.preventDefault();

    // Создание объекта FormData из формы
    const formData = new FormData(this);

    // Добавляем правильное имя поля для идентификатора, если необходимо
    const identifierType = formData.get("identifierType");
    let identifier = formData.get("identifier");
    if (identifierType === "username") {
        formData.set("username", identifier); // предполагаем, что сервер ожидает поле с именем "username"
    } else {
        formData.set("cardNumber", identifier); // предполагаем, что сервер ожидает поле с именем "cardNumber"
    }
    formData.delete("identifier"); // Удаление исходного поля "identifier", если необходимо

    fetch('/transfer', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if(data.success === "true") {
                showNotification("Транзакция успешно выполнена");
            } else {
                document.getElementById("error-message").textContent = data.message;
                document.getElementById("error-message").style.display = "block";
            }
        })
        .catch(error => {
            console.error('Ошибка:', error);
            showNotification("Ошибка при выполнении транзакции");
        });
});

function displayOperationDetails(operationId) {
    console.log('Displaying details for operation ID:', operationId);
    // Remove the 'active' class from all entries
    document.querySelectorAll('.operation-entry').forEach(entry => {
        entry.classList.remove('active');
    });

    // Add 'active' class to the selected entry
    const selectedEntry = document.querySelector(`[data-id="${operationId}"]`);
    if (selectedEntry) {
        selectedEntry.classList.add('active');
    }

    // Fetch and display operation details
    fetch(`/operation-details/${operationId}`)
        .then(response => response.json())
        .then(data => {
            console.log('Operation data:', data); // Debug: Log the data to see its structure

            const detailsDiv = document.getElementById('operation-details');
            let detailsHTML = ``;


            // Adjusted to check for enum names "SEND" and "RECEIVED" directly
            if (data.transactionType === 'SEND') {
                detailsHTML += `
                <p>Type: Transaction (Send)</p>
                <p>Recipient: ${data.recipient || 'N/A'}</p>
                <p>Amount: ${data.amount ? `${data.amount} $` : 'N/A'}</p>
                <p>Comment: ${data.comment || 'N/A'}</p>
            `;
            } else if (data.transactionType === 'RECEIVED') {
                detailsHTML += `
                <p>Type: Transaction (Received)</p>
                <p>Sender: ${data.sender || 'N/A'}</p> <!-- Updated from 'Recipient' to 'Sender' -->
                <p>Amount: ${data.amount ? `${data.amount} $` : 'N/A'}</p>
                <p>Comment: ${data.comment || 'N/A'}</p>
            `;

                // Checks for "WITHDRAW" and "DEPOSIT" from DepositActions
            } else if (data.depositActions === 'WITHDRAW') {
                detailsHTML += `
                <p>Type: Withdrawal</p>
                <p>Amount: ${data.amount ? `${data.amount} $` : 'N/A'}</p>
                <p>Balance Before: ${data.userBalanceBeforeOperation ? `${data.userBalanceBeforeOperation} $` : 'N/A'}</p>
                <p>Balance After: ${data.userBalanceAfterOperation ? `${data.userBalanceAfterOperation} $` : 'N/A'}</p>
            `;
            } else if (data.depositActions === 'DEPOSIT') {
                detailsHTML += `
                <p>Type: Deposit</p>
                <p>Amount: ${data.amount ? `${data.amount} $` : 'N/A'}</p>
                <p>Balance Before: ${data.userBalanceBeforeOperation ? `${data.userBalanceBeforeOperation} $` : 'N/A'}</p>
                <p>Balance After: ${data.userBalanceAfterOperation ? `${data.userBalanceAfterOperation} $` : 'N/A'}</p>
            `;
            } else {
                // If the operation type or deposit action is undefined or not recognized
                detailsHTML += `<p>Type: ${data.transactionType || data.depositActions || 'N/A'}</p>`;
            }

            detailsDiv.innerHTML = detailsHTML;
        })
        .catch(error => {
            console.error('Error fetching operation details:', error);
            document.getElementById('operation-details').innerText = 'Error loading details';
        });

}


function updateInputField() {
    const identifierInput = document.getElementById('identifier');
    const isCardSelected = document.getElementById('card').checked;

    if (isCardSelected) {
        identifierInput.placeholder = "Номер карты";
        identifierInput.oninput = function () {
            formatCardNumber(this);
        };
    } else {
        identifierInput.placeholder = "Имя пользователя";
        identifierInput.oninput = null; // Убираем форматирование для имени пользователя
    }
}

function formatCardNumber(input) {
    let cardNumber = input.value.replace(/[^\d]/g, '').substring(0, 16);
    cardNumber = cardNumber !== '' ? cardNumber.match(/.{1,4}/g).join(' ') : '';
    input.value = cardNumber;
}

function showCreateDepositForm() {
    const createDepositSection = document.getElementById("create-deposit-section");
    if (createDepositSection.style.display === "none") {
        createDepositSection.style.display = "block";
    }
}


function hideCreateDepositForm() {
    document.getElementById('create-deposit-section').style.display = 'none';
    document.getElementById('deposits-list').style.display = 'block';
}





