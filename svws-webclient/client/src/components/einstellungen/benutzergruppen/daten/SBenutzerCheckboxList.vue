<template>
	<svws-ui-table :items="[]" :no-data="listBenutzer().size() === 0" no-data-text="Keine Benutzer zugewiesen." :columns="cols" scroll>
		<template #header>
			<div class="svws-ui-tr" style="grid-template-columns: 2fr 1fr;">
				<div class="svws-ui-td">
					<template v-if="spalteLinks">{{ listBenutzer().size() }} insgesamt</template>
					<template v-else>{{ listBenutzer().size() }} aktiv zugewiesen</template>
				</div>
				<div class="svws-ui-td" />
			</div>
		</template>
		<template #body>
			<template v-for="benutzer in listBenutzer()" :key="benutzer.id">
				<s-benutzer-checkbox :benutzer="benutzer"
					v-model="aktiv" :spalte-links="spalteLinks"
					:add-benutzer-to-benutzergruppe="addBenutzerToBenutzergruppe"
					:remove-benutzer-from-benutzergruppe="removeBenutzerFromBenutzergruppe"
					:goto-benutzer="gotoBenutzer" />
			</template>
		</template>
	</svws-ui-table>
	<slot />
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { List, BenutzerListeEintrag } from "@core";

	const props = defineProps<{
		listBenutzer: () => List<BenutzerListeEintrag>;
		title : string;
		spalteLinks : boolean;
		addBenutzerToBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
		removeBenutzerFromBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
		gotoBenutzer: (b_id: number) => Promise<void>;
		listBenutzerInGruppe: () => List<BenutzerListeEintrag>;
	}>();

	const aktiv = computed({
		get: () => true,
		set: (value) => {
			// TODO
		},
	});

	const cols = [
		{key: 'anzeigename', label: 'Anzeigename', span: 2},
		{key: 'name', label: ' ', span: 1},
	];

</script>
