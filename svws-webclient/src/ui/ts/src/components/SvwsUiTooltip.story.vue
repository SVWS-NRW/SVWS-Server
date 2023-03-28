<template>
	<Story title="SVWS UI/Tooltip">
		<div class="flex flex-col gap-4 items-start">
			<svws-ui-tooltip>
				Default Tooltip
				<template #content>
					Tooltip Content
				</template>
			</svws-ui-tooltip>
			<svws-ui-tooltip position="right">
				Tooltip auf der rechten Seite
				<template #content>
					Tooltip Content
				</template>
			</svws-ui-tooltip>
			<svws-ui-tooltip :show-arrow="false">
				Tooltip bubble ohne arrow
				<template #content>
					Tooltip Content
				</template>
			</svws-ui-tooltip>
			<svws-ui-tooltip indicator="help">
				Mit Help Icon
				<template #content>
					Tooltip Content
				</template>
			</svws-ui-tooltip>
			<svws-ui-tooltip indicator="info">
				Mit Info Icon
				<template #content>
					Tooltip Content
				</template>
			</svws-ui-tooltip>
			<svws-ui-tooltip indicator="danger" color="danger">
				Mit Danger Icon
				<template #content>
					Tooltip Content
				</template>
			</svws-ui-tooltip>
			<svws-ui-tooltip :hover="false">
				Disabled Hover
				<template #content>
					Tooltip Content
				</template>
			</svws-ui-tooltip>
			<svws-ui-tooltip color="dark">
				Dark Tooltip
				<template #content>
					Tooltip Content
				</template>
			</svws-ui-tooltip>
			<svws-ui-tooltip color="primary">
				Primary Color Tooltip
				<template #content>
					Tooltip Content
				</template>
			</svws-ui-tooltip>
			<svws-ui-tooltip color="danger">
				Danger Color Tooltip
				<template #content>
					Tooltip Content
				</template>
			</svws-ui-tooltip>
		</div>
	</Story>
</template>
