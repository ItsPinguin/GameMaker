# Resources
## Item Template
```json
{
  "type": "item_template",
  "id": "your_item_id",
  "material": "YOUR_MATERIAL",
  "name": "Your Item Name",
  "lore": [
    "Multiple",
    "Lines"
  ],
  "actions": [],
  "skull_texture": "kgshdkhgfhksdgfhkgsdhkgfshjgdjfhgsjhdgfhsgdhjsdg"
}
```

## Menu Template
```json
{
  "type": "menu_template",
  "id": "your_menu_id",
  "title": "Menu Title",
  "inventory_type": "INVENTORY_TYPE",
  "rows": 6,
  "overlay_font": "namespace:custom_font",
  "overlay_char": "\u1234",
  "contents": [
    {
      "item": "inline or reference",
      "slots": [
        "row 0",
        "r:1",
        20, 21, 22,
        "fill"
      ],
      "actions": [],
      "list": "your item list id"
    }
  ]
}
```

## Notification
```json
{
  "messages": [
    {
      "message": "your message",
      "delay": 1234
    }
  ],
  "titles": [
    {
      "title": "your title",
      "subtitle": "your subtitle",
      "fade_in": 12,
      "stay": 34,
      "fade_out": 56,
      "delay": 123
    },
    {
      "action_bar": "your action bar",
      "delay": 456
    }
  ]
}
```

## Action
```json
{
  "action": "your_action_id",
  "parameter1": "value1",
  "parameter2": "value2"
}
```

## I18n
```json
{
  "type": "lang",
  "id": "your_lang_id",
  "locale": "your_locale",
  "translations": {
    "path": {
      "to": {
        "key": "value here",
        "multiple_lines": [
          "line1",
          "line2"
        ],
        "variables": "my name is %s"
      }
    }
  }
}
```