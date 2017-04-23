# java -cp "lib/*":"build/classes/main" jade.Boot -gui -agents "DatabaseAgent:sau.hw.ai.agents.DatabaseAgent;UIAgent:sau.hw.ai.agents.UIAgent"
java \
    -Dswing.aatext=true -Dswing.plaf.metal.controlFont="Noto Sans UI-17" -Dswing.plaf.metal.userFont="Noto Sans UI-17" \
    -cp "lib/*":"build/classes/main" \
    jade.Boot -nomtp -agents \
    "DatabaseAgent:sau.hw.ai.agents.DatabaseAgent;UIAgent:sau.hw.ai.agents.UIAgent;WeatherAgent:sau.hw.ai.agents.WeatherAgent"
