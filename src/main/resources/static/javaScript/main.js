document.addEventListener("DOMContentLoaded", function() {
    document.cookie.split(";").forEach(function(c) {
        document.cookie = c.trim().split("=")[0] + "=;expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/";
    });
});

function displayTransactionDetails(transactionId) {
    fetch(`/transaction-details/${transactionId}`)
        .then(response => response.json())
        .then(data => {
            const detailsDiv = document.getElementById('transaction-details');
            detailsDiv.innerHTML = `
            <p>Sender: ${data.sender}</p>
            <p>Recipient: ${data.recipient}</p>
            <p>Amount: ${data.amount} $</p>
            <p>Date: ${data.operationDate}</p>
            <p>Comment: ${data.comment}</p>
          
        `;
        })
        .catch(error => console.error('Error fetching transaction details:', error));
}


document.addEventListener('DOMContentLoaded', function () {
    const buttons = document.querySelectorAll('.custom-btn');
    const windows = {
        'transfer-btn': document.getElementById('transfer-window'),
        'banks-btn': document.getElementById('banks-window'),
        'currency-btn': document.getElementById('currency-window'),
    };
    let lastOpenedWindow = null;
    const historyElement = document.getElementById('gray-rectangle'); // Идентификатор элемента с историей транзакций
    const transactionDetailsElement = document.getElementById('transaction-details'); // Идентификатор элемента с информацией о транзакции

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


function formatCardNumber(input) {
    let cardNumber = input.value.replace(/[^\d]/g, '').substring(0, 16);
    cardNumber = cardNumber !== '' ? cardNumber.match(/.{1,4}/g).join(' ') : '';
    input.value = cardNumber;
}


function showCreateBankForm() {
    document.getElementById('create-bank-section').style.display = 'block';
    document.getElementById('banks-list').style.display = 'none';
}

function hideCreateBankForm() {
    document.getElementById('create-bank-section').style.display = 'none';
    document.getElementById('banks-list').style.display = 'block';
}

