# onReceivedCommand List

<br>

## CLICK_PRODUCT_DETAIL
> delivered when product is selected from product list

<br>

```
command:
    CLICK_PRODUCT_DETAIL

payload:
    {
        action = link;
        brand = brand;
        sku = product01234;
        url = "http://product.com";
    }
```
<br>
<br>

## CLICK_PRODUCT_CART
> delivered when cart button is clicked from product list

<br>

```
command:
    CLICK_PRODUCT_CART

payload:
    {
        action = LINK;
        brand = "company brand";
        ...
        url = "https://company.com/product/12345/detail";
    }
```

<br>
<br>

## LOGIN_REQUIRED
> delivered when login is required

<br>

```
command:
    LOGIN_REQUIRED

payload:
    none
```

<br>
<br>

## ON_SUCCESS_CAMPAIGN_JOIN
> delivered on successful campaign entry

<br>

```
command:
    ON_SUCCESS_CAMPAIGN_JOIN

payload:
    {
        isGuest = true or false
    }
```

<br>
<br>
<br>
<br>
