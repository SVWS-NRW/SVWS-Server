<template>
	<div class="svws-ui-tr" role="row" @click.prevent="spalteLinks ? add() : void 0"
		:class="spalteLinks ? 'text-ui-contrast-50 hover:text-ui-contrast-100 cursor-copy' : ''"
		:title="spalteLinks ? 'Benutzer zur Gruppe hinzufügen' : 'Benutzer aus Gruppe entfernen'">
		<div class="svws-ui-td" role="cell">
			<svws-ui-button type="icon" size="small" title="Benutzer anzeigen" @click.stop="gotoBenutzer(benutzer.id)">
				<span class="icon i-ri-link" />
			</svws-ui-button>
			{{ benutzer.anzeigename }}
		</div>
		<div class="svws-ui-td" role="cell">
			{{ benutzer.name }}
			<svws-ui-button v-if="!spalteLinks" type="icon" class="ml-auto" @click.prevent="add()">
				<span class="icon i-ri-delete-bin-line" />
			</svws-ui-button>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type {BenutzerListeEintrag } from "@core";

	const props = defineProps<{
		benutzer: BenutzerListeEintrag;
		spalteLinks : boolean;
		addBenutzerToBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
		removeBenutzerFromBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
		gotoBenutzer: (b_id: number) => Promise<void>;
	}>();

	async function add() {
		if (props.spalteLinks)
			await props.addBenutzerToBenutzergruppe(props.benutzer);
		else
			await props.removeBenutzerFromBenutzergruppe(props.benutzer);
	}

</script>
