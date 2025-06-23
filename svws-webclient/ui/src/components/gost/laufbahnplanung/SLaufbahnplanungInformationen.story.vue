<template>
	<Story title="Laufbahnplanungsinformationen" :layout="{ type: 'single', iframe: false }">
		<Variant title="Fehlermeldungen" id="default">
			<s-laufbahnplanung-informationen :fehlerliste="() => fehlerliste" />
		</Variant>
	</Story>
</template>

<script setup lang="ts">

	import { GostBelegpruefungErgebnisFehler } from '../../../../../core/src/core/abschluss/gost/GostBelegpruefungErgebnisFehler';
	import { GostBelegungsfehlerArt } from '../../../../../core/src/core/abschluss/gost/GostBelegungsfehlerArt';
	import { ArrayList } from '../../../../../core/src/java/util/ArrayList';
	import type { List } from '../../../../../core/src/java/util/List';

	const fehlerliste: List<GostBelegpruefungErgebnisFehler> = new ArrayList()
	const f1 = new GostBelegpruefungErgebnisFehler();
	f1.art = GostBelegungsfehlerArt.HINWEIS.kuerzel;
	f1.beschreibung = "Testfehler 1"
	const f2 = new GostBelegpruefungErgebnisFehler();
	f2.art = GostBelegungsfehlerArt.HINWEIS.kuerzel;
	f2.beschreibung = "Testfehler 2"
	fehlerliste.add(f1);
	fehlerliste.add(f2);

</script>
