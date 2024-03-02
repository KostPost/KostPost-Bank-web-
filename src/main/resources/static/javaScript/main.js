function displayTransactionDetails(transactionId) {
    fetch(`/transaction-details/${transactionId}`)
        .then(response => response.json())
        .then(data => {
            const detailsDiv = document.getElementById('transaction-details');
            detailsDiv.innerHTML = `
            <p>Sender: ${data.sender}</p>
            <p>Recipient: ${data.recipient}</p>
            <p>Amount: ${data.transferSum}</p>
            <p>Date: ${data.transactionDate}</p>
        `;
        })
        .catch(error => console.error('Error fetching transaction details:', error));
}
