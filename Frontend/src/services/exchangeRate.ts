// src/services/exchangeRate.ts
import Freecurrencyapi from '@everapi/freecurrencyapi-js';

const currencyapi = new Freecurrencyapi(import.meta.env.VITE_API_KEY);
const baseCurrency = 'EUR';
const targetCurrency = 'USD';

export let exchangeRate = 0;

currencyapi.latest({
    base_currency: baseCurrency,
    currencies: targetCurrency
}).then((response: any) => {
    if (response && response.data && response.data.USD) {
        exchangeRate = response.data.USD;
    } else {
        console.error('Ungültige API-Antwort:', response);
    }
}).catch((error: any) => {
    console.error('Fehler beim Abrufen der Wechselkurse:', error);
});
