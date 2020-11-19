const {ClientVoiceManager, RichEmbed, Client, Message} = require("discord.js");
const Discord = require("discord.js");
const server = require("minecraft-server-util");

const bot = new Client();

const token = "Njc2NTUyNjk0NzkzNzY0ODY1.XkHWzA.-HZsljdvoXFOJcQPXoynebiwYj0";

const PREFIX = '?';

bot.on('ready', () => {
    console.log("bot is online")
})

bot.on('message', (Message) => {
    let args = Message.content.substring(PREFIX.length).split(' ');

    switch(args[0]){
        case 'status':
            server.status('51.68.204.237')
            .then((response) => {
                console.log(response);
                    
                let embed = new Discord.MessageEmbed()
                    .setColor('#5d7c15')
                    .setTitle('Dragon Realm');
                let players = response.samplePlayers;
                console.log(players);
                if(players.length > 0){
                    let online = "";
                    players.forEach( (player) => {
                        online = online + ` - ${player.name}\n`
                    });
                    embed.addField("Online players: ", " \n" + online);
                }
                else{
                    embed.addField("Online players: ", 
                    `no online players`);
                }
                    
                console.log("message send")
                Message.channel.send(embed);
            })
            .catch(error => {
                throw error;
            });
        break;
    }

})

bot.login(token);
