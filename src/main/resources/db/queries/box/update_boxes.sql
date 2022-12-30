UPDATE box
SET opened = :opened,
    winning = :winning,
    picked = :picked
WHERE id = :id;
