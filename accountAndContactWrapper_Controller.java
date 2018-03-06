public with sharing class AccountSearchController {
    
    @AuraEnabled
    public static List<Account> getAccounts() {
        return [select id, name from Account];
    }
    
    
    @AuraEnabled
    public static List<accountsAndContacts> getAccountsAndContacts() {
        List<accountsAndContacts> accountsAndContactsList = new List<accountsAndContacts>();
        
        for (Account accountRecordItem: [SELECT id, Name, BillingCountry, ShippingCountry, Type, CreatedDate, (SELECT id, Name FROM Contacts) FROM Account]) {
            if (accountRecordItem.Contacts.isEmpty()) {
            	accountsAndContacts accountsAndContactsSet = new accountsAndContacts(accountRecordItem, new Contact()); 
                accountsAndContactsList.add(accountsAndContactsSet);
            }
            for (Contact contactRecordItem: accountRecordItem.Contacts) {
                accountsAndContacts accountsAndContactsSet = new accountsAndContacts(accountRecordItem, contactRecordItem);
                accountsAndContactsList.add(accountsAndContactsSet);
            }
        }
        return accountsAndContactsList;
    }

    public class accountsAndContacts {
        @AuraEnabled public Account accountRecordItem { get; set; }
        @AuraEnabled public Contact contactRecordItem { get; set; }
        
        public accountsAndContacts(Account accountRecordItemParam, Contact contactRecordItemParam) {
            accountRecordItem = accountRecordItemParam;
            contactRecordItem = contactRecordItemParam;
        }    
    }
}