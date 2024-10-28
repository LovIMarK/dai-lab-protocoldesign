# Protocole de Communication Client-Serveur pour Calculs

## Étape 1 : Vue d'ensemble

### Objectif
Développer un protocole permettant à un client de demander des opérations mathématiques basiques (addition et multiplication) à un serveur. Le client envoie une commande contenant l'opération et les opérandes ; le serveur interprète cette commande, exécute le calcul, puis renvoie le résultat au client.

### Comportement général
- Le serveur démarre, écoute les connexions entrantes et envoie un message de bienvenue au client contenant les opérations supportées.
- Le client se connecte, affiche les opérations supportées, puis attend les instructions de l'utilisateur pour envoyer une commande au serveur.
- Le serveur traite la commande, renvoie le résultat, et attend la prochaine instruction.

## Étape 2 : Protocole de la couche de transport

- **Connexion** : le client et le serveur communiquent via le protocole TCP.
- **Port** : à définir dans la configuration ; par défaut, utiliser le port 12345.
- **Adresse IP** : l’adresse IP du serveur doit être fournie dans la configuration du client.
- **Fermeture de la connexion** :
  - Le client peut envoyer la commande `QUIT` pour terminer la connexion.
  - Si la connexion se termine brusquement ou en cas d'erreur, le serveur envoie un message d'erreur spécifique.

## Étape 3 : Messages

### Format des messages
- **Encodage** : UTF-8
- **Fin de ligne** : Chaque message se termine par un retour à la ligne `\n`.

### Format de commande (client vers serveur)
- Les messages envoyés suivent ce format :  
  `OPERATION operande1 operande2`
  
  - **OPERATION** : L’opération à effectuer (`ADD` ou `MUL`).
  - **operande1** et **operande2** : Les deux nombres (entiers ou décimaux) sur lesquels opérer.

#### Exemples
- `ADD 10 20\n` : additionne 10 et 20.
- `MUL 5 3\n` : multiplie 5 et 3.

### Réponse du serveur
Le serveur renvoie un message de confirmation contenant le résultat ou un message d'erreur en cas de format de commande invalide.

- **Réponse standard** :  
  `RESULT valeur`
  
- **Message d'erreur** :  
  `ERROR description`

#### Exemples de réponses
- `RESULT 30\n` : résultat pour `ADD 10 20`.
- `ERROR Unknown operation\n` : si une commande est inconnue.
- `ERROR Invalid format\n` : si la syntaxe de la commande n'est pas correcte.

### Message de bienvenue du serveur
À la connexion, le serveur envoie un message de bienvenue spécifiant les opérations supportées :
  - `WELCOME Supported operations: ADD, MUL\n`

### Exemples de commandes et réponses
1. **Connexion et bienvenue**
   - Serveur : `WELCOME Supported operations: ADD, MUL\n`
  
2. **Commande d'addition**
   - Client : `ADD 15 25\n`
   - Serveur : `RESULT 40\n`

3. **Commande de multiplication**
   - Client : `MUL 3 7\n`
   - Serveur : `RESULT 21\n`

4. **Commande inconnue**
   - Client : `SUB 10 5\n`
   - Serveur : `ERROR Unknown operation\n`

5. **Fermeture de connexion**
   - Client : `QUIT\n`
   - Serveur : (fermeture de la connexion)

## Étape 4 : Exemples détaillés

Pour clarifier le comportement :

### Exemples complets

#### Exemple 1 : Addition
1. **Client** se connecte, reçoit :  
   `WELCOME Supported operations: ADD, MUL\n`
2. **Client** envoie :  
   `ADD 10 20\n`
3. **Serveur** répond :  
   `RESULT 30\n`

#### Exemple 2 : Erreur de format
1. **Client** se connecte, reçoit :  
   `WELCOME Supported operations: ADD, MUL\n`
2. **Client** envoie :  
   `ADD 10\n`  (nombre d'opérandes incorrect)
3. **Serveur** répond :  
   `ERROR Invalid format\n`
