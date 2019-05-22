jQuery("#simulation")
  .on("click", ".s-fcf4bf23-2aa8-4067-9692-9d475fa35f4d .click", function(event, data) {
    var jEvent, jFirer, cases;
    if(data === undefined) { data = event; }
    jEvent = jimEvent(event);
    jFirer = jEvent.getEventFirer();
    if(jFirer.is("#s-Callout_1")) {
      cases = [
        {
          "blocks": [
            {
              "actions": [
                {
                  "action": "jimNavigation",
                  "parameter": {
                    "target": "screens/3b6da12c-4f4a-4004-b276-55d7c6d85cfb"
                  },
                  "exectype": "serial",
                  "delay": 0
                }
              ]
            }
          ],
          "exectype": "serial",
          "delay": 0
        }
      ];
      event.data = data;
      jEvent.launchCases(cases);
    }
  })
  .on("click", ".s-fcf4bf23-2aa8-4067-9692-9d475fa35f4d .toggle", function(event, data) {
    var jEvent, jFirer, cases;
    if(data === undefined) { data = event; }
    jEvent = jimEvent(event);
    jFirer = jEvent.getEventFirer();
    if(jFirer.is("#s-Ellipse_1")) {
      if(jFirer.data("jimHasToggle")) {
        jFirer.removeData("jimHasToggle");
        jEvent.undoCases(jFirer);
      } else {
        jFirer.data("jimHasToggle", true);
        event.backupState = true;
        event.target = jFirer;
        cases = [
          {
            "blocks": [
              {
                "actions": [
                  {
                    "action": "jimHide",
                    "parameter": {
                      "target": [ "#s-Ellipse_2" ]
                    },
                    "exectype": "serial",
                    "delay": 0
                  },
                  {
                    "action": "jimChangeStyle",
                    "parameter": [ {
                      "#s-fcf4bf23-2aa8-4067-9692-9d475fa35f4d #s-Ellipse_1": {
                        "attributes": {
                          "stroke": "#757575"
                        }
                      }
                    },{
                      "#s-fcf4bf23-2aa8-4067-9692-9d475fa35f4d #s-Ellipse_1": {
                        "attributes-ie": {
                          "stroke": "#757575"
                        }
                      }
                    } ],
                    "exectype": "parallel",
                    "delay": 0
                  }
                ]
              }
            ],
            "exectype": "serial",
            "delay": 0
          }
        ];
        jEvent.launchCases(cases);
      }
    }
  });