import React from 'react';

const Cart = ({ cartItems, onRemoveItem, onCheckout }) => {
    const totalPrice = cartItems.reduce((total, item) => total + item.price * item.quantity, 0);

    return (
        <div className="cart">
            <h2>Your Cart</h2>
            {cartItems.length === 0 ? (
                <p>Your cart is empty.</p>
            ) : (
                <ul>
                    {cartItems.map(item => (
                        <li key={item.id}>
                            <span>{item.name} - ${item.price} x {item.quantity}</span>
                            <button onClick={() => onRemoveItem(item.id)}>Remove</button>
                        </li>
                    ))}
                </ul>
            )}
            <h3>Total: ${totalPrice.toFixed(2)}</h3>
            <button onClick={onCheckout} disabled={cartItems.length === 0}>Checkout</button>
        </div>
    );
};

export default Cart;