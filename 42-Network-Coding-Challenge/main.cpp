#include <iostream>
#include <string>
#include <vector>
#include <algorithm>

using namespace std;

/**
 * Grab Snaffles and try to throw them through the opponent's goal!
 * Move towards a Snaffle to grab it and use your team id to determine towards where you need to throw it.
 * Use the Wingardium spell to move things around at your leisure, the more magic you put it, the further they'll move.
 **/

int main()
{
    int myTeamId; // if 0 you need to score on the right of the map, if 1 you need to score on the left
    cin >> myTeamId; cin.ignore();

    // game loop
    while (1) {
        int myScore;
        int myMagic;
        cin >> myScore >> myMagic; cin.ignore();
        int opponentScore;
        int opponentMagic;
        cin >> opponentScore >> opponentMagic; cin.ignore();
        int entities; // number of entities still in game
        cin >> entities; cin.ignore();
        for (int i = 0; i < entities; i++) {
            int entityId; // entity identifier
            string entityType; // "WIZARD", "OPPONENT_WIZARD" or "SNAFFLE" or "BLUDGER"
            int x; // position
            int y; // position
            int vx; // velocity
            int vy; // velocity
            int state; // 1 if the wizard is holding a Snaffle, 0 otherwise. 1 if the Snaffle is being held, 0 otherwise. id of the last victim of the bludger.
            cin >> entityId >> entityType >> x >> y >> vx >> vy >> state; cin.ignore();
        }
        for (int i = 0; i < 2; i++) {

            // Write an action using cout. DON'T FORGET THE "<< endl"
            // To debug: cerr << "Debug messages..." << endl;


            // Edit this line to indicate the action for each wizard (0 ≤ thrust ≤ 150, 0 ≤ power ≤ 500, 0 ≤ magic ≤ 1500)
            // i.e.: "MOVE x y thrust" or "THROW x y power" or "WINGARDIUM id x y magic"
            cout << "MOVE 8000 3750 100" << endl;
        }
    }
}
