# Resources
## Item Template
```json
{
  "type": "item_template",
  "id": "your_item_id",
  "material": "diamond_sword",
  "components": {
    "enchantements": {
      "sharpness": 5
    },
    "rarity": "epic",
    "custom_data": {
      "PublicBukkitValues": {
        "your_plugin:key": "value"
      }
    }
  },
  "removed_components": [
    "weapon",
    "attribute_modifiers"
  ],
  "custom_data": {
    "name": "Your Item Name",
    "lore": [
      "Multiple",
      "Lines"
    ],
    "actions": []
  },
  "amount": 2
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
  "interaction_triggers": ["left_clic_block", "right_click_air", "..."],
  "inventory_triggers": ["right", "shift_left", "swap_offhand", "..."],
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