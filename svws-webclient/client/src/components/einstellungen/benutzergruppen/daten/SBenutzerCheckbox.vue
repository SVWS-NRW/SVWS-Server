<template>
	<tr class="svws-ui-tr" @click.prevent="add"
		style="grid-template-columns: 2fr 1fr;" :class="styleClasses" :title>
		<td class="svws-ui-td">
			<svws-ui-button type="icon" size="small" title="Benutzer anzeigen" @click.stop="gotoBenutzer(benutzer.id)">
				<span class="icon i-ri-link" />
			</svws-ui-button>
			{{ benutzer.anzeigename }}
		</td>
		<td class="svws-ui-td">
			{{ benutzer.name }}
			<svws-ui-button v-if="!spalteLinks" type="icon" class="ml-auto" @click.prevent="remove" :disabled>
				<span class="icon i-ri-delete-bin-line" />
			</svws-ui-button>
		</td>
	</tr>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { BenutzerListeEintrag } from "@core/core/data/benutzer/BenutzerListeEintrag";

	const props = defineProps<{
		benutzer: BenutzerListeEintrag;
		spalteLinks: boolean;
		addBenutzerToBenutzergruppe: (benutzer: BenutzerListeEintrag) => Promise<void>;
		removeBenutzerFromBenutzergruppe: (benutzer: BenutzerListeEintrag) => Promise<void>;
		gotoBenutzer: (idBenutzer: number) => Promise<void>;
		disabled: boolean;
	}>();

	const title = computed(() => {
		if (props.spalteLinks) {
			return props.disabled ? 'Sie können sich selbst zu keiner Gruppe hinzufügen' : 'Benutzer zur Gruppe hinzufügen';
		} else {
			return props.disabled ? 'Sie können sich selbst aus keiner Gruppe entfernen' : 'Benutzer aus Gruppe entfernen';
		}
	});

	const styleClasses = computed(() => {
		let classes = '';
		if (props.spalteLinks) {
			classes += 'text-ui-50 hover:text-ui-100 cursor-copy';
		}

		if (props.disabled) {
			classes += ' cursor-not-allowed';
		}
		return classes;
	});

	const add = async () => {
		if (props.spalteLinks && !props.disabled) {
			await props.addBenutzerToBenutzergruppe(props.benutzer);
		}
	};

	const remove = async () => {
		if (!props.spalteLinks && !props.disabled) {
			await props.removeBenutzerFromBenutzergruppe(props.benutzer);
		}
	};

</script>
