<template>
	<Story title="Laufbahnplanungsinformationen">
		<Variant title="Fehlermeldungen">
			<s-laufbahnplanung-informationen :fehlerliste="fehlerliste" />
		</Variant>
	</Story>
</template>

<script setup lang="ts">

	import { GostBelegpruefungErgebnisFehler, GostBelegungsfehlerArt, ArrayList } from "@svws-nrw/svws-core";

	const fehlerliste: ArrayList<GostBelegpruefungErgebnisFehler> = new ArrayList()
	const f1 = new GostBelegpruefungErgebnisFehler();
	f1.art = GostBelegungsfehlerArt.HINWEIS.kuerzel;
	f1.beschreibung = "Testfehler 1"
	const f2 = new GostBelegpruefungErgebnisFehler();
	f2.art = GostBelegungsfehlerArt.HINWEIS.kuerzel;
	f2.beschreibung = "Testfehler 2"
	fehlerliste.add(f1);
	fehlerliste.add(f2);
</script>
