{
    "filter": {
        "criterias": [
            {
                "operator": "GE",
                "property": "birth",
                "value": "1995-01-01",
                "format": "yyyy-MM-dd",
                "valueType": "LOCALDATE"
            },
            {
                "operator": "LE",
                "property": "birth",
                "value": "1995-03-02",
                "format": "yyyy-MM-dd",
                "valueType": "LOCALDATE"
            }
        ]
    },
    "pageRequest": {
        "page": 0,
        "size": 10
    },
    "sort": {
    "orders": [
        {
        "property": "id",
        "direction": "DESC"
        }
    ]
    }
}

{
    "filter": {
        "criterias": [
            {
            "operator": "ILIKE",
            "property": "blockCode",
            "value": "%123%"
            },
            {
            "operator": "ISNULL",
            "property": "deletedDate"
            }
        ]
    },
    "pageRequest": {
        "page": 0,
        "size": 10
    },
    "sort": {
    "orders": []
    }
}