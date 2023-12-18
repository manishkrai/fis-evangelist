package com.fis.evangelist.subscription;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fis.evangelist.subscription.controller.SubscriptionController;
import com.fis.evangelist.subscription.entity.Subscription;
import com.fis.evangelist.subscription.model.SubscriptionResponse;
import com.fis.evangelist.subscription.service.SubscriptionService;

@ExtendWith(MockitoExtension.class)
public class SubscriptionControllerTest {
	
	@InjectMocks
	private SubscriptionController subscriptionController;
	
	@Mock
	private SubscriptionService subscriptionService;
		
	private MockMvc mockMvc;
	
	private SubscriptionResponse subscriptionResponse;
	
	private Subscription subscription;
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	@BeforeEach
    public void setup(){
		this.subscriptionResponse = new SubscriptionResponse();
		this.subscriptionResponse.setSubscriberName("Manish Rai");
		this.subscriptionResponse.setBookId("B1212");
		this.subscriptionResponse.setDateSubscribed(new Date());
		
		
		this.subscription = new Subscription();
		this.subscription.setSubscriberName("Manish Rai");
		this.subscription.setBookId("B1212");
		this.subscription.setDateSubscribed(new Date());
		this.mockMvc = MockMvcBuilders.standaloneSetup(subscriptionController).build();
    }
	
	@Test 
	void addSubscription() throws Exception {	
		Mockito.when(subscriptionService.addSubscription(ArgumentMatchers.any())).thenReturn(this.subscriptionResponse);		
		
		String json = mapper.writeValueAsString(this.subscriptionResponse);
        mockMvc.perform(post("/subscriptions")
        		.contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                .content(json).accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isCreated())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        		.andExpect(jsonPath("$.bookId").value("B1212"))
        		.andExpect(jsonPath("$.subscriberName").value("Manish Rai"));
	}
	
	@Test 
	void getAllSubscriptions() throws Exception {	
		List<SubscriptionResponse> subscriptions = new ArrayList<SubscriptionResponse>();
		subscriptions.add(subscriptionResponse);
		
		Mockito.when(subscriptionService.getAllSubscriptions()).thenReturn(subscriptions);		
		
		 String json = mapper.writeValueAsString(this.subscription);
        mockMvc.perform(get("/subscriptions")
                .content(json).accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isOk())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        		.andExpect(jsonPath("$[*].bookId").value("B1212"))
        		.andExpect(jsonPath("$[*].subscriberName").value("Manish Rai"));
	}
	
	@Test 
	void getSubscription() throws Exception {	
		Mockito.when(subscriptionService.getSubscription(ArgumentMatchers.any())).thenReturn(this.subscriptionResponse);
        mockMvc.perform(get("/subscriptions/addSubscription", "Manish Rai")
        		.accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isOk())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        		.andExpect(jsonPath("$.bookId").value("B1212"))
        		.andExpect(jsonPath("$.subscriberName").value("Manish Rai"));
	        		
	}
	
	@Test 
	void addSubscriptionWithFeign() throws Exception {	
		Mockito.when(subscriptionService.addSubscriptionWithFeign(ArgumentMatchers.any())).thenReturn(this.subscriptionResponse);		
		
		String json = mapper.writeValueAsString(this.subscription);
        mockMvc.perform(post("/subscriptions/feign")
        		.contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
                .content(json).accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isCreated())
        		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        		.andExpect(jsonPath("$.bookId").value("B1212"))
        		.andExpect(jsonPath("$.subscriberName").value("Manish Rai"));
	}
	
	
}
