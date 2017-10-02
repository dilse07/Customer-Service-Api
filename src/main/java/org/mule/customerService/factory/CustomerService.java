package org.mule.customerService.factory;

import org.mule.customerService.customers.Customer;

import org.mule.module.apikit.exception.MuleRestException;
import org.mule.module.apikit.exception.NotFoundException;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class CustomerService {

    private static final Logger LOGGER = Logger.getLogger(CustomerService.class);
    private long customerCount = 0;

    private HashMap<String, Customer> customers = new HashMap<String, Customer>();

    @SuppressWarnings("unchecked")
    public void initialise() {
        
    	JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("customers-sample.json")));
            JSONArray customers = (JSONArray) jsonObject.get("customers");
            Iterator<JSONObject> iterator = customers.iterator();
            while(iterator.hasNext()) {
              initialiseCustomerBuffer(iterator.next());
            }
            
            customerCount = customers.size();

        } catch (Exception e) {
            LOGGER.error("Error initializing CustomerMgmtSystem. Cause: ", e);
        }
    }

	private void initialiseCustomerBuffer(JSONObject jsonObject) {
       
    	Customer customer = new Customer();
        customer.setId("" + jsonObject.get("customerID"));
        customer.setFirstName((String) jsonObject.get("firstName"));
        customer.setLastName((String) jsonObject.get("lastName"));
        customer.setAddress((String) jsonObject.get("address"));
        
        this.customers.put(customer.getId(), customer);
    }

    public List<Customer> getCustomers() throws MuleRestException {
        
    	if (this.customers.isEmpty()) {
        	throw new NotFoundException("No customers are found.");
        }
    	
    	List<Customer> customers = new ArrayList<Customer>();
        customers.addAll(this.customers.values());
        return customers;
    }

    public Customer getCustomer(String id) throws MuleRestException {
        
    	if(!customerExists(id)) {
            throw new NotFoundException("Customer " + id + " does not exist.");
        }
    	
        return customers.get(id);
    }
    
    public void createCustomer(HashMap<String, String> customer) {
    	
    	Customer c = new Customer();
        c.setId("" + (getCustomerCount() + 1));
        c.setFirstName(customer.get("firstName"));
        c.setLastName(customer.get("lastName"));
        c.setAddress(customer.get("address"));
        
        customers.put(c.getId(), c);
        
        customerCount++;
    }
    
    public void updateCustomer(String id, HashMap<String, String> customer) throws MuleRestException {
    	
        if(!customerExists(id)) {
            throw new NotFoundException("Customer " + id + " does not exist.");
        }
        
        Customer cust = getCustomer(id);
        
        if (cust.getFirstName() != null){
        	cust.setFirstName(customer.get("firstName"));
        }
        
        if (cust.getLastName() != null){
        	cust.setLastName(customer.get("lastName"));
        }

        if (cust.getAddress() != null){
        	cust.setAddress(customer.get("address"));
        }
        
    }

    public void deleteCustomer(String id) throws MuleRestException {
        if(!customerExists(id)) {
            throw new NotFoundException("Customer " + id + " does not exist.");
        }
    	
        customers.remove(id);
    }
    
    public boolean customerExists(String id) {
        return customers.containsKey(id);
    }
    
    public long getCustomerCount(){
    	return customerCount;
    }
}

