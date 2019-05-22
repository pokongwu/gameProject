(function(window, undefined) {
  var dictionary = {
    "4fe81611-d3ea-412f-94ab-25a1971e8b0f": "Map",
    "fcf4bf23-2aa8-4067-9692-9d475fa35f4d": "Map8",
    "ecaa23b5-27f8-4bbf-8559-0cffdbd71c94": "Map7",
    "d12245cc-1680-458d-89dd-4f0d7fb22724": "Main",
    "8526bc36-d77e-4abc-968c-749cba934f53": "Map6",
    "e24db54e-bd23-4b24-9574-4e40d48394da": "Map5",
    "cd5b779e-2df0-4104-a9b2-b5dd92ff667e": "Map4",
    "6e87722a-876a-45ab-82f1-ba64bd94d05e": "Map3",
    "5f6d4879-4845-4625-8561-738c97ee9a28": "Map2",
    "3b6da12c-4f4a-4004-b276-55d7c6d85cfb": "victory",
    "bebbc281-ba50-48f9-9fb2-b2cb6037d05d": "Map4.5",
    "9986f9ef-0c85-4218-8aff-782172b67e8b": "Options",
    "f39803f7-df02-4169-93eb-7547fb8c961a": "Template 1",
    "bb8abf58-f55e-472d-af05-a7d1bb0cc014": "default"
  };

  var uriRE = /^(\/#)?(screens|templates|masters|scenarios)\/(.*)(\.html)?/;
  window.lookUpURL = function(fragment) {
    var matches = uriRE.exec(fragment || "") || [],
        folder = matches[2] || "",
        canvas = matches[3] || "",
        name, url;
    if(dictionary.hasOwnProperty(canvas)) { /* search by name */
      url = folder + "/" + canvas;
    }
    return url;
  };

  window.lookUpName = function(fragment) {
    var matches = uriRE.exec(fragment || "") || [],
        folder = matches[2] || "",
        canvas = matches[3] || "",
        name, canvasName;
    if(dictionary.hasOwnProperty(canvas)) { /* search by name */
      canvasName = dictionary[canvas];
    }
    return canvasName;
  };
})(window);