<template>
	<div class="page page-flex-col">
		<Teleport to=".svws-ui-header--actions" v-if="isMounted">
			<svws-ui-modal-hilfe> <hilfe-notenmodul-konfiguration /> </svws-ui-modal-hilfe>
		</Teleport>
		<Teleport to=".svws-sub-nav-target" v-if="isMounted">
			<svws-ui-sub-nav :focus-switching-enabled :focus-help-visible>
				<svws-ui-button type="transparent" @click.stop="setzeKonfigurationDefault()" title="Sperrt in der Konfiguration alle Noteneingaben." class="text-ui-100 subNavigationFocusField">
					<span class="icon-sm i-ri-checkbox-blank-circle-line" /> Alles sperren
				</svws-ui-button>
				<svws-ui-button type="transparent" @click.stop="setzeKonfigurationAllesErlauben()" title="Erlaubt in der Konfiguration alle Noteneingaben." class="text-ui-100 subNavigationFocusField">
					<span class="icon-sm i-ri-checkbox-circle-line" /> Alles freischalten
				</svws-ui-button>
				<svws-ui-button v-if="!istLokal" type="transparent" @click.stop="setzeKonfigurationLokal()" title="Synchronisiert die Konfiguration für die Noteneingaben und die Sichtbarkeit mit dem lokalen Notenmodul." class="text-ui-100 subNavigationFocusField">
					<span class="icon-sm i-ri-loop-left-line" /> Übernahme der lokalen Konfiguration
				</svws-ui-button>
			</svws-ui-sub-nav>
		</Teleport>
		<div class="overflow-y-scroll">
			<div class="min-w-fit flex flex-col gap-8">
				<notenmodul-konfiguration-sichtbarkeit :manager="managerSichtbareSpalten" />
				<notenmodul-konfiguration-sperrungen :manager="managerSperrungen" />
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { onMounted, ref } from 'vue';
	import { useRegionSwitch } from '@ui';
	import type { NotenmodulKonfigurationProps } from './NotenmodulKonfigurationProps';

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const props = defineProps<NotenmodulKonfigurationProps>();

	async function setzeKonfigurationDefault() {
		await props.managerSperrungen().setConfigDefault();
	}

	async function setzeKonfigurationAllesErlauben() {
		await props.managerSperrungen().setConfigAllowAll();
	}

	async function setzeKonfigurationLokal() {
		await props.syncWithLocalConfig();
	}

</script>
