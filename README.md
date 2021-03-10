# shoppingcart

1) use mvn clean install to build the project
2) Using embedded h2.
3) Supporting API's
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
              
   2) Update-cart: To update the existing cart with item details - 
     

