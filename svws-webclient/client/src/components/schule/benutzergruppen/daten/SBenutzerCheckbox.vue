<template>
	<div class="svws-ui-tr" role="row" @click.prevent="spalteLinks ? add() : void 0"
		:class="spalteLinks ? 'text-black/50 hover:text-black dark:text-white/50 dark:hover:text-white cursor-copy' : ''"
		:title="spalteLinks ? 'Benutzer zur Gruppe hinzufügen' : 'Benutzer aus Gruppe entfernen'">
		<div class="svws-ui-td" role="cell">
			<svws-ui-button type="icon" size="small" title="Benutzer anzeigen" @click.stop="goToBenutzer(benutzer.id)">
				<i-ri-link />
			</svws-ui-button>
			{{ benutzer.anzeigename }}
		</div>
		<div class="svws-ui-td" role="cell">
			{{ benutzer.name }}
			<svws-ui-button v-if="!spalteLinks" type="icon" class="ml-auto" @click.prevent="add()">
				<i-ri-delete-bin-line />
			</svws-ui-button>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type {BenutzerListeEintrag } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		benutzer: BenutzerListeEintrag;
		spalteLinks : boolean;
		addBenutzerToBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
		removeBenutzerFromBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
		goToBenutzer: (b_id: number) => Promise<void>;
	}>();

	async function add() {
		props.spalteLinks
			? await props.addBenutzerToBenutzergruppe(props.benutzer)
			: await props.removeBenutzerFromBenutzergruppe(props.benutzer)
	}

</script>
