package com.artificialbyte.animaliano.services.epayco;

import androidx.annotation.NonNull;

import co.epayco.android.models.Card;
import co.epayco.android.models.Charge;
import co.epayco.android.models.Client;

public class Transaction {

    private Card card;
    private Client client;
    private Charge charge;

    public Transaction() {
    }

    public Transaction(@NonNull Card card, @NonNull Client client, @NonNull Charge charge) {
        this.card = card;
        this.client = client;
        this.charge = charge;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Charge getCharge() {
        return charge;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }

    public static Transaction getAcceptedCard(){
        Transaction transaction = new Transaction();

        Card card = new Card();
        card.setNumber("4575623182290326");
        card.setMonth("12");
        card.setYear("2025");
        card.setCvc("123");
        transaction.setCard(card);

        Client client = new Client();
        client.setName("Cliente epayco acepted");
        client.setEmail("acepted@epayco.co");
        client.setPhone("305274321");
        client.setDefaultCard(true);
        transaction.setClient(client);

        Charge charge = new Charge();
        charge.setDocType("CC");
        charge.setDocNumber("1035851980");
        charge.setName("Acepted");
        charge.setLastName("Card");
        charge.setEmail("example@email.com");
        charge.setInvoice("AC-1234");
        charge.setDescription("Test Payment");
        charge.setValue("116000");
        charge.setTax("16000");
        charge.setTaxBase("100000");
        charge.setCurrency("COP");
        charge.setDues("12");
        charge.setIp("190.000.000.000");
        transaction.setCharge(charge);

        return transaction;
    }

    public static Transaction getNotFoundsCard(){
        Transaction transaction = new Transaction();

        Card card = new Card();
        card.setNumber("4151611527583283");
        card.setMonth("12");
        card.setYear("2025");
        card.setCvc("123");
        transaction.setCard(card);

        Client client = new Client();
        client.setCustomer_id("mipropioid");
        client.setName("Cliente epayco NotFounds");
        client.setEmail("notfound@epayco.co");
        client.setPhone("305274321");
        client.setDefaultCard(true);
        transaction.setClient(client);

        Charge charge = new Charge();
        charge.setDocType("CC");
        charge.setDocNumber("1035851980");
        charge.setName("Not");
        charge.setLastName("Founds");
        charge.setEmail("example@email.com");
        charge.setInvoice("NF-1234");
        charge.setDescription("Test Payment");
        charge.setValue("116000");
        charge.setTax("16000");
        charge.setTaxBase("100000");
        charge.setCurrency("COP");
        charge.setDues("12");
        charge.setIp("190.000.000.000");
        transaction.setCharge(charge);

        return transaction;
    }

    public static Transaction getFailedCard(){
        Transaction transaction = new Transaction();

        Card card = new Card();
        card.setNumber("5170394490379427");
        card.setMonth("12");
        card.setYear("2025");
        card.setCvc("123");
        transaction.setCard(card);

        Client client = new Client();
        client.setCustomer_id("mipropioid");
        client.setName("Cliente epayco Failed");
        client.setEmail("failed@epayco.co");
        client.setPhone("305274321");
        client.setDefaultCard(true);
        transaction.setClient(client);

        Charge charge = new Charge();
        charge.setDocType("CC");
        charge.setDocNumber("1035851980");
        charge.setName("Failed");
        charge.setLastName("Card");
        charge.setEmail("example@email.com");
        charge.setDescription("Test Payment");
        charge.setValue("116000");
        charge.setTax("16000");
        charge.setTaxBase("100000");
        charge.setCurrency("COP");
        charge.setDues("12");
        charge.setIp("190.000.000.000");
        transaction.setCharge(charge);

        return transaction;
    }

    public static Transaction getPendingCard(){
        Transaction transaction = new Transaction();

        Card card = new Card();
        card.setNumber("373118856457642");
        card.setMonth("12");
        card.setYear("2025");
        card.setCvc("123");
        transaction.setCard(card);

        Client client = new Client();
        client.setCustomer_id("mipropioid");
        client.setName("Cliente epayco Pending");
        client.setEmail("pending@epayco.co");
        client.setPhone("305274321");
        client.setDefaultCard(true);
        transaction.setClient(client);

        Charge charge = new Charge();
        charge.setDocType("CC");
        charge.setDocNumber("1035851980");
        charge.setName("Pending");
        charge.setLastName("Card");
        charge.setEmail("example@email.com");
        charge.setDescription("Test Payment");
        charge.setValue("116000");
        charge.setTax("16000");
        charge.setTaxBase("100000");
        charge.setCurrency("COP");
        charge.setDues("12");
        charge.setIp("190.000.000.000");
        transaction.setCharge(charge);

        return transaction;
    }
}
