<template>
	<svws-ui-table :items="[]" :no-data="benutzerList().isEmpty()" no-data-text="Keine Benutzer zugewiesen." :columns="cols" scroll>
		<template #header>
			<div class="svws-ui-tr" style="grid-template-columns: 2fr 1fr;">
				<div class="svws-ui-td">
					<template v-if="spalteLinks">{{ benutzerList().size() }} insgesamt</template>
					<template v-else>{{ benutzerList().size() }} aktiv zugewiesen</template>
				</div>
				<div class="svws-ui-td" />
			</div>
		</template>
		<template #body>
			<template v-for="benutzer in benutzerList()" :key="benutzer.id">
				<s-benutzer-checkbox :benutzer
					:spalte-links
					:add-benutzer-to-benutzergruppe
					:remove-benutzer-from-benutzergruppe
					:goto-benutzer
					:disabled="benutzer.id === benutzerState.benutzerdaten.id" />
			</template>
		</template>
	</svws-ui-table>
	<slot />
</template>

<script setup lang="ts">
	import type { BenutzerListeEintrag } from '@core/core/data/benutzer/BenutzerListeEintrag';
	import type { List } from '@core/java/util/List';
	import { useBenutzerState } from '@ui/states/BenutzerState';



	defineProps<{
		title: string;
		spalteLinks: boolean;
		benutzerList: () => List<BenutzerListeEintrag>;
		addBenutzerToBenutzergruppe: (benutzer: BenutzerListeEintrag) => Promise<void>;
		removeBenutzerFromBenutzergruppe: (benutzer: BenutzerListeEintrag) => Promise<void>;
		gotoBenutzer: (idBenutzer: number) => Promise<void>;
	}>();

	const benutzerState = useBenutzerState();

	const cols = [
		{ key: 'anzeigename', label: 'Anzeigename', span: 2 },
		{ key: 'name', label: ' ', span: 1 },
	];

</script>
