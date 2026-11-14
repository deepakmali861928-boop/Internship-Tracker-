// This file handles the checkout process, including gathering user information and preparing the order for submission.

document.addEventListener('DOMContentLoaded', () => {
    const checkoutForm = document.getElementById('checkout-form');
    const orderSummary = document.getElementById('order-summary');
    const totalAmount = document.getElementById('total-amount');

    checkoutForm.addEventListener('submit', (event) => {
        event.preventDefault();
        const formData = new FormData(checkoutForm);
        const orderDetails = {};

        formData.forEach((value, key) => {
            orderDetails[key] = value;
        });

        displayOrderSummary(orderDetails);
    });

    function displayOrderSummary(orderDetails) {
        orderSummary.innerHTML = `
            <h2>Order Summary</h2>
            <p>Name: ${orderDetails.name}</p>
            <p>Address: ${orderDetails.address}</p>
            <p>Payment Method: ${orderDetails.paymentMethod}</p>
            <p>Total Amount: $${totalAmount.textContent}</p>
        `;
    }
});