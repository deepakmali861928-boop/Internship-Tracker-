import React, { useState } from 'react';

const CheckoutForm = () => {
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        address: '',
        paymentMethod: 'creditCard',
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here, such as calling the payment gateway
        console.log('Form submitted:', formData);
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Checkout</h2>
            <div>
                <label>
                    Name:
                    <input type="text" name="name" value={formData.name} onChange={handleChange} required />
                </label>
            </div>
            <div>
                <label>
                    Email:
                    <input type="email" name="email" value={formData.email} onChange={handleChange} required />
                </label>
            </div>
            <div>
                <label>
                    Address:
                    <textarea name="address" value={formData.address} onChange={handleChange} required />
                </label>
            </div>
            <div>
                <label>
                    Payment Method:
                    <select name="paymentMethod" value={formData.paymentMethod} onChange={handleChange}>
                        <option value="creditCard">Credit Card</option>
                        <option value="paypal">PayPal</option>
                    </select>
                </label>
            </div>
            <button type="submit">Complete Purchase</button>
        </form>
    );
};

export default CheckoutForm;