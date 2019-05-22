jQuery("#simulation")
  .on("click", ".s-3b6da12c-4f4a-4004-b276-55d7c6d85cfb .click", function(event, data) {
    var jEvent, jFirer, cases;
    if(data === undefined) { data = event; }
    jEvent = jimEvent(event);
    jFirer = jEvent.getEventFirer();
    if(jFirer.is("#s-raised_Button_2")) {
      cases = [
        {
          "blocks": [
            {
              "actions": [
                {
                  "action": "jimChangeStyle",
                  "parameter": [ {
                    "#s-3b6da12c-4f4a-4004-b276-55d7c6d85cfb #s-raised_Button_2 > .backgroundLayer": {
                      "attributes": {
                        "background-color": "#3700B3",
                        "background-attachment": "scroll",
                        "box-shadow": "0px 5px 15px 0px #999999"
                      }
                    }
                  },{
                    "#s-3b6da12c-4f4a-4004-b276-55d7c6d85cfb #s-raised_Button_2": {
                      "attributes": {
                        "text-shadow": "none"
                      }
                    }
                  },{
                    "#s-3b6da12c-4f4a-4004-b276-55d7c6d85cfb #s-raised_Button_2": {
                      "attributes-ie": {
                        "-pie-background": "#3700B3",
                        "-pie-poll": "false"
                      }
                    }
                  } ],
                  "exectype": "serial",
                  "delay": 0
                },
                {
                  "action": "jimChangeStyle",
                  "parameter": [ {
                    "#s-3b6da12c-4f4a-4004-b276-55d7c6d85cfb #s-raised_Button_2 > .backgroundLayer": {
                      "attributes": {
                        "background-color": "#6200EE",
                        "background-attachment": "scroll",
                        "box-shadow": "0px 2px 5px 0px #999999"
                      }
                    }
                  },{
                    "#s-3b6da12c-4f4a-4004-b276-55d7c6d85cfb #s-raised_Button_2": {
                      "attributes": {
                        "text-shadow": "none"
                      }
                    }
                  },{
                    "#s-3b6da12c-4f4a-4004-b276-55d7c6d85cfb #s-raised_Button_2": {
                      "attributes-ie": {
                        "-pie-background": "#6200EE",
                        "-pie-poll": "false"
                      }
                    }
                  } ],
                  "exectype": "timed",
                  "delay": 200
                }
              ]
            }
          ],
          "exectype": "serial",
          "delay": 0
        },
        {
          "blocks": [
            {
              "actions": [
                {
                  "action": "jimNavigation",
                  "parameter": {
                    "target": "screens/d12245cc-1680-458d-89dd-4f0d7fb22724"
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
  });