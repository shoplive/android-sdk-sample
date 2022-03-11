# onReceivedCommand 목록

<br>

## CLICK_PRODUCT_DETAIL
> 상품 목록에서 상품을 선택했을 때 전달

<br>

```
command:
    CLICK_PRODUCT_DETAIL

payload:
    {
        action = 링크;
        brand = 브랜드;
        sku = product01234;
        url = "http://product.com";
    }
```
<br>
<br>

## CLICK_PRODUCT_CART
> 상품목록의 장바구니 버튼을 클릭했을 때 전달

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
> 로그인이 필요한 경우 전달

<br>

```
command:
    LOGIN_REQUIRED

payload:
    없음
```

<br>
<br>

## ON_SUCCESS_CAMPAIGN_JOIN
> 방송 진입에 성공했을 때 전달

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