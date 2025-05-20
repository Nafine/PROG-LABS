

#!/bin/bash

# Shell-скрипт для подключения к серверу и выполнения команд

SERVER="itmo"  # Имя сервера или IP-адрес
SSH_OPTIONS="-T"  # Дополнительные опции SSH (-T отключает псевдо-TTY)

# Команды для выполнения на удаленном сервере
REMOTE_COMMANDS=$(cat <<EOF
cd prog/lab7 || { echo "Ошибка: не удалось перейти в директорию prog/lab7"; exit 1; }
source setup/env_set.sh || { echo "Ошибка: не удалось выполнить env_set.sh"; exit 1; }
java -jar lab8-server.jar || { echo "Ошибка: не удалось запустить lab8-server.jar"; exit 1; }
EOF
)

# Выполнение команд через SSH
ssh $SSH_OPTIONS $SERVER <<EOF
$REMOTE_COMMANDS
EOF

# Проверка статуса выполнения
if [ $? -eq 0 ]; then
    echo "Команды успешно выполнены на сервере $SERVER"
else
    echo "Произошла ошибка при выполнении команд на сервере $SERVER" >&2
    exit 1
fi

