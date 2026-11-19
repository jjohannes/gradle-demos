package starfleet.db.app;

import module starfleet.db.layout;

import starfleet.db.data.SearchDb;

class StarfleetDatabase {

    static void main() {
        MemberLayout.use(SearchDb::searchByName);
    }
}
