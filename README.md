# shoppingcart

1) Clone the project from repo - git clone https://github.com/testabridge/shoppingcart.git
2) go to project - cd shoppingcart 
3) use mvn clean install to build the project
4) Supporting API's
    1) For adding cart -- <<hostname>>/cart/add-cart
          Http Method: Post
          Sample Body: 
               {
                  "items": [
                      {
                          "name": "LEDTV",
                          "description": "LEDTelevision",
                          "unit_price": 1000,
                          "quantity": 2
                      },
                      {
                          "name": "LG AC",
                          "description": "LG Air Conditioner",
                          "unit_price": 1000,
                          "quantity": 10
                      }
                  ],
                  "deliveryaddress": "Gudivada",
                  "owner": "test",
                  "price": 50.5
              }
         In case of Success, api will return cart json response, else error message as like below formate
         
         Error Response:
              {"code":500,"message":"Got Exception while adding cart"}
              
   2) Search-cart: To search a cart with fiter criteria - <<host-name>>/cart/search-cart?owner=test&address=test&status=Active
        Http Method: Get
         
         In case of Success, api will return cart with details, else error message as like below formate
         
         Error Response:
              {"code":500,"message":"Got Exception while fetching cart"}  
   3) Delete-cart: To delete an existing cart - <<host-name>>/cart/delete-cart/{cartId}
        Http Method: Delete
         
         In case of Success, api will return success message, else error message as like below formate
         
         Error Response:
              {"code":500,"message":"Got Exception while deleting cart"}  
   4) update-cart: To update an existing cart with item details - <<host-name>>/cart/update-cart/{cartId}
                    
        Http Method: Put
          Sample Body: 
               {
                  "items": [
                      {
                          "name": "LEDTV",
                          "description": "LEDTelevision",
                          "unit_price": 1000,
                          "quantity": 2
                      },
                      {
                          "name": "LG AC",
                          "description": "LG Air Conditioner",
                          "unit_price": 1000,
                          "quantity": 10
                      }
                  ]
              }
         In case of Success, api will return cart json response, else error message as like below formate
         
         Error Response:
              {"code":500,"message":"Got Exception while updating cart"}
  5) Description: I am using h2 embedded data base. Implemented cart expairy functionality and check sum of cart. We cant delete/update an expired cart. lifetime of a cart is 3 hours. Also we have another API to search an cart by ID. (<<host-name>>/cart/{cartId}). And did unit testing using junit and mockito. Mostly covered all scenarios. 
  
     

