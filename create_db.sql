CREATE TABLE IF NOT EXISTS films (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(2000),
    href VARCHAR(2000),
    detail VARCHAR(5000),
    magnet VARCHAR(2000),
    magnetWeburl VARCHAR(2000),
    imageList VARCHAR(5000),
    updateTime LONG,
    extra VARCHAR(100)
);