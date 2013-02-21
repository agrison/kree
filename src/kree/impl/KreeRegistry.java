package kree.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.IPartService;

public class KreeRegistry {
    Map<IPartService, KreeIndentGuide> registry = new HashMap<IPartService, KreeIndentGuide>();
}
