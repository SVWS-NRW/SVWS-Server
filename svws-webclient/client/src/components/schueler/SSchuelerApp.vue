<template>
	<div v-if="(schuelerListeManager().hasDaten() && (activeRouteType === RouteType.DEFAULT)) || (activeRouteType !== RouteType.DEFAULT)" class="page--flex">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<template v-if="activeRouteType === RouteType.DEFAULT">
					<svws-ui-avatar :src="'data:image/png;base64, ' + foto" :alt="foto !== undefined ? 'Foto von ' + vorname + ' ' + nachname : ''" upload capture />
					<div v-if="schuelerListeManager().hasDaten()" class="svws-headline-wrapper">
						<h2 class="svws-headline">
							<span>{{ vorname }} {{ nachname }}</span>
							<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
								ID:
								{{ schuelerListeManager().daten().id }}
							</svws-ui-badge>
						</h2>
						<span class="svws-subline">{{ inputKlasse === null ? '—' : inputKlasse }}</span>
					</div>
					<div v-if="schuelerListeManager().daten().keineAuskunftAnDritte" class="svws-headline-wrapper">
						<span class="icon-xxl icon-error i-ri-alert-line inline-block" />
						<span class="text-error content-center"> Keine Auskunft an Dritte </span>
					</div>
				</template>

				<template v-else-if="activeRouteType === RouteType.HINZUFUEGEN">
					<div class="svws-headline-wrapper">
						<h2 class="svws-headline">
							<span>Neuen Schüler anlegen...</span>
						</h2>
					</div>
				</template>

				<template v-else-if="activeRouteType === RouteType.GRUPPENPROZESSE">
					<div class="svws-headline-wrapper">
						<h2 class="svws-headline">Gruppenprozesse</h2>
						<span class="svws-subline">{{ schuelerSubline }}</span>
					</div>
				</template>
			</div>
			<div class="svws-ui-header--actions" />
		</header>

		<svws-ui-tab-bar :tab-manager>
			<router-view />
		</svws-ui-tab-bar>
	</div>

	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-group-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { SchuelerAppProps } from "./SSchuelerAppProps";
	import { RouteType } from "~/router/RouteType";

	const props = defineProps<SchuelerAppProps>();

	const schuelerSubline = computed(() => {
		const auswahlSchuelerList = props.schuelerListeManager().liste.auswahl();
		const leadingSchuelerList = [];
		for (let index = 0; index < auswahlSchuelerList.size(); index++) {
			if (index > 2)
				break;

			leadingSchuelerList.push(`${auswahlSchuelerList.get(index).vorname} ${auswahlSchuelerList.get(index).nachname}`);
		}

		let subline = leadingSchuelerList.join(', ');
		if (auswahlSchuelerList.size() > 3)
			subline += ` und ${auswahlSchuelerList.size() - 3} Weitere`;

		return subline;
	})

	const foto = computed<string | undefined>(() => {
		return props.schuelerListeManager().daten().foto ?? undefined;
	});

	const nachname = computed<string>(() => {
		return props.schuelerListeManager().daten().nachname;
	});

	const vorname = computed<string>(() => {
		return props.schuelerListeManager().daten().vorname;
	});

	const inputKlasse = computed<string | null>(() => {
		if (!props.schuelerListeManager().hasDaten())
			return null;
		return props.schuelerListeManager().klassen.get(props.schuelerListeManager().auswahl().idKlasse)?.kuerzel ?? null;
	});

</script>
